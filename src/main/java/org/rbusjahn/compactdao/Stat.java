package org.rbusjahn.compactdao;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class Stat {
	
	Logger LOG = Logger.getLogger(getClass());
	
	private Set<Long> times = new HashSet<>();
	
	@Around("execution(* *(..)) && @annotation(com.jcabi.aspects.Loggable)")
	  public Object around(ProceedingJoinPoint point) throws Throwable {
	    long start = System.currentTimeMillis();
	    
	    Object result = point.proceed();
	    
	    long time = (System.currentTimeMillis() - start);
	    times.add(time);
	    
	    String sResult = "";
	    if(result != null){
	    	sResult = result.toString();
	    }
	    LOG.info(
	      String.format("Statistics: #%s(%s): %s in %s",
	      MethodSignature.class.cast(point.getSignature()).getMethod().getName(),
	      point.getArgs().toString(),
	      sResult,
	      ""+time
	    ));
	      
	    return result;
	  }
}
