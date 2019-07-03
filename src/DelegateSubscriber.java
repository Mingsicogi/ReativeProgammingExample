import java.util.concurrent.Flow;

public class DelegateSubscriber<T, R> implements Flow.Subscriber<T> {

	Flow.Subscriber subscriber;

	public DelegateSubscriber(Flow.Subscriber<? super R> subscriber){
		this.subscriber = subscriber;
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		subscriber.onSubscribe(subscription);
	}

	@Override
	public void onNext(T item) {
		subscriber.onNext(item);
	}

	@Override
	public void onError(Throwable throwable) {
		subscriber.onError(throwable);
	}

	@Override
	public void onComplete() {
		subscriber.onComplete();
	}
}
