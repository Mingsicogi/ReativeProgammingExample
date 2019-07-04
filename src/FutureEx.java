import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureEx {

	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Hello");

		System.out.println("Exit");
	}
}
