package com.macys.platform.rxjava.training;

import java.util.function.Consumer;

import org.junit.Test;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class BackpressureTest {

	Observable<Long> crazedObservable = Observable.create(new ObservableOnSubscribe<Long>() {
		long count = 0;

		@Override
		public void subscribe(ObservableEmitter<Long> e) throws Exception {
			System.out.println("Thread Name : " + Thread.currentThread().getName());
			while (true) {
				e.onNext(count);
				count++;
			}

		}

	});

	@Test
	public void testObservableBackpressure() throws Exception {
		crazedObservable.observeOn(Schedulers.newThread())
				// .doOnNext(System.out.println("Thread Name : " +
				// Thread.currentThread().getName()))
				.subscribe(aLong -> {
					Thread.sleep(5);
					System.out.println("Received :" + aLong);
				});
	}

	Flowable<Long> crazedFlowable = Flowable.create(new FlowableOnSubscribe<Long>() {
		long count = 0;

		@Override
		public void subscribe(FlowableEmitter<Long> e) throws Exception {
			System.out.println("Thread Name : " + Thread.currentThread().getName());
			while (true) {
				e.onNext(count);
				count++;
			}
		}
	}, BackpressureStrategy.DROP  //LATEST, ERROR, 
			);
	

	@Test
	public void testFlowableBackpressure() throws Exception {
		crazedFlowable.observeOn(Schedulers.newThread())
				// .doOnNext(System.out.println("Thread Name : " +
				// Thread.currentThread().getName()))
				.subscribe(aLong -> {
					Thread.sleep(5);
					System.out.println("Received :" + aLong);
				});
	}

}
