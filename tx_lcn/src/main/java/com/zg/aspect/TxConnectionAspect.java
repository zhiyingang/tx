package com.zg.aspect;

import com.zg.tx.TxConnection;
import com.zg.tx.TxManager;
import com.zg.tx.TxTransactional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Created by Administrator on 2018/11/28.
 */
@Aspect
@Component
public class TxConnectionAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint point){
        try {
            Connection connection =  (Connection)point.proceed();
            TxTransactional txTransactional = TxManager.getTxTransactional();
            return new TxConnection(connection,txTransactional);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return null;
    }

}
