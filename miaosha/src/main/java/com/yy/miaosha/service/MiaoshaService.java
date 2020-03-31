package com.yy.miaosha.service;

import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.redis.prefix.MiaoshaKey;
import com.yy.miaosha.utils.UUIDUtil;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-10 15:15
 **/
@Service
public class MiaoshaService {
    @Resource
    GoodsService goodsService;
    @Resource
    OrderService orderService;
    @Resource
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goods) {

        //1. 减库存
        boolean res = goodsService.reduceStock(goods);
        if (res) {
            OrderInfo orderInfo = orderService.createOrder(user, goods);
            //2. 生成新订单并返回
            return orderInfo;
        }else {
            //给Redis中添加一个该商品已卖完的标记
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getMiaoshaResult(long userId, long goodsId) {
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        if (miaoshaOrder != null) {
            //秒杀成功
            return miaoshaOrder.getOrderId();
        }else {
            //判断是不是已经卖完了，卖完了就不需要再轮询了，直接返回-1，前端识别到-1就不会轮询了；
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            }else {
                //代表商品没卖完，还没秒杀到，这种情况就是还在排队中，前端识别到0后会继续轮询；
                return 0;
            }
        }
    }

    /**
     * 判断某个商品是否还有库存
     * @param
     * @return
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }

    public boolean checkPath(MiaoshaUser user, Long goodsId, String path) {
        if(user==null || path==null) {
            return false;
        }
        String oldPath = redisService.get(MiaoshaKey.getMiaoshaPath, ""+user.getId()+goodsId, String.class);
        return oldPath.equals(path);
    }

    public String createPath(MiaoshaUser user, Long goodsId) {
        String path = UUIDUtil.uuid();
        redisService.set(MiaoshaKey.getMiaoshaPath, ""+user.getId()+goodsId, path);
        return path;
    }

    public BufferedImage createVerifyCode(MiaoshaUser user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // 也就是在生成的图片上随机打50个点，干扰视线嘛！
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // 生成随机的数学公式
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码的计算结果存到redis中
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, rnd);
        //输出图片
        return image;
    }


    private static char[] ops = new char[] {'+', '-', '*'};

    /**
     * 生成随机数学公式，就是三个数的简单运算
     * @param rdm
     * @return
     */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }

    /**
     * 用Script引擎来计算出数学公式的值，这里就是用了一个技巧而已，方便计算，也可以自己写一个，但是没必要
     * @param exp
     * @return
     */
    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 验证验证码是否正确
     * @param user
     * @param goodsId
     * @param verifyCode
     * @return
     */
    public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
        if(user == null || goodsId <=0) {
            return false;
        }
        Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId, Integer.class);
        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId()+","+goodsId);
        return true;
    }

}