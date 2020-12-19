// Создание класса реурса с использованием блокировок 
// для изменения и получения ресурса в потокобезопасном режиме.
// При получении значения ресурс обнуляется, при присвоении ему значения ожидаем обнуления ресурса...
public class Resource <T> {

  // Ресурс
  T res;

  // Получение значения ресурса при условии нахождения значения в нем...
  synchronized T get() throws InterruptedException {

    // Монитор(использование терминов wait, notify)...
    if(res == null)
      this.wait();
    notify();
    T t = this.res;
    this.res = null;
    return t;
  }

  // Присвоение ресурсу значения, при условии что оно не равно null...
  synchronized  void put(T n) throws InterruptedException {

    // Монитор...
    if(res != null)
      wait();
    res = n;
    notify();
  }

  public static void main(String[] args) {

    // Создаем объект с ресурсом...
    Resource<Integer> resource = new Resource<>();

    // Создаем поток для присвоения ресурсу значений...
    new Thread(() -> {
      for(int i = 0; i < 10; i++){
        try {
          // Синхронизируем работу потока...
          synchronized (resource) {
            resource.put(i);
            System.out.println("Put : " + i);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

    // Создаем поток для получения ресурса...
    new Thread(() -> {
      for(int i = 0; i < 10; i++){
        try {
          // Синхронизируем работу потока...
          synchronized (resource) {
            Integer integer = resource.get();
            System.out.println("Get : " + integer);
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }
}