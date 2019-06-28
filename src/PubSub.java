
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

		//java
		Publisher publisher = getPublisher(Stream.iterate(1, i -> i + 1).limit(10).collect(Collectors.toList()));
		publisher.subscribe(getSubscriber());
	}

	private static Publisher getPublisher(Iterable<Integer> iterable) {
		return (subscriber) -> subscriber.onSubscribe(new Subscription() {
//			Iterator<Integer> it = iterable.iterator();

			// call back 방식으로 결과를 수행하기 위함.
			// back pressure : publisher 와 subscriber 사이에 속도 차를 조절하기 위한 기술.
			@Override
			public void request(long n) {
				// n : 얼마만큼 보낼지 값
//				while (n-- > 0){
//					if(it.hasNext()){
//						subscriber.onNext(it.next());
//					} else {
//						subscriber.onComplete();
//						break;
//					}
//				}
				try{
					iterable.forEach(num -> subscriber.onNext(num));
					subscriber.onComplete();

				} catch (Throwable e){
					subscriber.onError(e);
				}
			}

			@Override
			public void cancel() {

			}
		});
	}

	private static Subscriber<Integer> getSubscriber() {
		return new Subscriber<>() {
			Subscription subscription;

			@Override
			public void onSubscribe(Subscription subscription) {
				System.out.println("onSubscribe");
				this.subscription = subscription;
				subscription.request(Long.MAX_VALUE);
			}

			@Override
			public void onNext(Integer item) { // Observer의 update와 같음.
				System.out.println("onNext : " + item);
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
	}

}
