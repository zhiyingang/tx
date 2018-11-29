package com.zg.aspect;

import com.zg.tx.TransactionType;
import com.zg.tx.TxManager;
import com.zg.tx.TxTransaction;
import com.zg.tx.TxTransactional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018/11/28.
 */
@Aspect
@Component
public class TxTransactionAspect implements Ordered{

    @Around("@annotation(com.zg.tx.TxTransaction)")
    public void invoke(ProceedingJoinPoint point){
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        TxTransaction txTransaction = method.getAnnotation(TxTransaction.class);
        System.out.println(txTransaction.isStart());
        String groupId = "";
        if(txTransaction.isStart()){
            groupId = TxManager.createGroup();
        }else{
            groupId = TxManager.getGroupLocal();
        }
        TxTransactional txTransactional = TxManager.createTransation(groupId);

        try {
            //走spring逻辑
            point.proceed();

            TxManager.submitTransation(txTransactional,txTransaction.isEnd(), TransactionType.commit);
        } catch (Throwable throwable) {
            TxManager.submitTransation(txTransactional,txTransaction.isEnd(), TransactionType.rollback);
            throwable.printStackTrace();
        }

    }

    @Override
    public int getOrder() {
        return 1000;
    }
}
