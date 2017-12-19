package com.macys.platform.rxjava.training;

import org.junit.Test;

import io.reactivex.Maybe;
import rx.Observable;

public class AggregateTest {
	
	
	@Test
	public void testReduce() throws Exception {
		Maybe<Integer> reduce = Observable.range(1, 5).reduce((total, next) -> {
			System.out.format("Total =%t, Next=%n", total, next);
			return total = total * next;
		});
		
		System.out.println(reduce.defaultIfEmpty(-1).toBlocking());
		//Thread.sleep(50000);
	}

}
