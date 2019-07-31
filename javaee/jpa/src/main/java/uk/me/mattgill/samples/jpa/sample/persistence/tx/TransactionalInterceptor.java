package uk.me.mattgill.samples.jpa.sample.persistence.tx;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Transactional
@Interceptor
public class TransactionalInterceptor implements Serializable {

    @Resource
    private UserTransaction utx;
    
    @AroundInvoke
    public Object manageTransaction(InvocationContext ctx) throws Exception {
        startTransaction();
        Object next = ctx.proceed();
        endTransaction();
        return next;
    }
    
    public void startTransaction() {
        try {
            utx.begin();
        } catch (NotSupportedException ex) {
            Logger.getLogger(Transactional.class.getName()).log(Level.SEVERE, "Nested transactions not supported.", ex);
        } catch (SystemException ex) {
            Logger.getLogger(Transactional.class.getName()).log(Level.SEVERE, "Unexpected error in starting transaction.", ex);
        }
    }
    
    public void endTransaction() {
        try {
            utx.commit();
        } catch (RollbackException ex) {
            Logger.getLogger(Transactional.class.getName()).log(Level.SEVERE, "Transaction was rolled back.", ex);
        } catch (HeuristicMixedException | HeuristicRollbackException ex) {
            Logger.getLogger(Transactional.class.getName()).log(Level.SEVERE, "Heuristic transaction committed.", ex);
        } catch (SecurityException ex) {
            Logger.getLogger(Transactional.class.getName()).log(Level.SEVERE, "Thread is not allowed to commit transaction.", ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(Transactional.class.getName()).log(Level.SEVERE, "Thread is not associated with a transaction.", ex);
        } catch (SystemException ex) {
            Logger.getLogger(Transactional.class.getName()).log(Level.SEVERE, "Unexpected error in committing transaction.", ex);
        }
    }
    
}
