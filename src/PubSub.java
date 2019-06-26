
import java.util.Arrays;
import java.util.Iterator;

import static java.util.concurrent.Flow.*;

/**
 * Rx(확장된 Observable 이라 불릴 수 있을 만큼 많은 부분의 기능이 같음)
 *
 * @author minssogi
 */
public class PubSub {
	public static void main(String[] args) {
		// Publisher <- Observable
		// Subscriber <- Observer

		Iterable<Integer> iterable = Arrays.asList(1,2,3,4,5,6,7,8);

		//java
		Publisher publisher = (subscriber) -> subscriber.onSubscribe(new Subscription() {

			Iterator<Integer> it = iterable.iterator();

			// call back 방식으로 결과를 수행하기 위함.
			// back pressure : publisher 와 subscriber 사이에 속도 차를 조절하기 위한 기술.
			@Override
			public void request(long n) {
				// n : 얼마만큼 보낼지 값
				while (n-- > 0){
					if(it.hasNext()){
						subscriber.onNext(it.next());
					} else {
						subscriber.onComplete();
						break;
					}
				}
			}

			@Override
			public void cancel() {

			}
		});


		Subscriber<Integer> subscriber = new Subscriber<>() {
			int bufferSize = 2;
			Subscription subscription;
			@Override
			public void onSubscribe(Subscription subscription) {
				System.out.println("onSubscribe");
				this.subscription = subscription;
				subscription.request(2);
			}

			@Override
			public void onNext(Integer item) { // Observer의 update와 같음.
				System.out.println("onNext : " + item);
				if(--bufferSize <= 0){
					subscription.request(2);
					bufferSize = 2;
				}
			}

			@Override
			public void onError(Throwable throwable) { // 에러
				System.out.println("onEroor : " + throwable.getMessage());
			}

			@Override
			public void onComplete() { // 완료시, 데이터 전달이 끝났을때
				System.out.println("onComplete");
			}
		};

		publisher.subscribe(subscriber);
	}

}
