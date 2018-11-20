package Study.HighGrade;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Volatile修饰的成员变量在每次被线程访问时，都强迫从主内存中重读该成员变量的值。
 * transient是Java语言的关键字，用来表示一个域不是该对象串行化的一部分。当一个对象被串行化的时候，transient型变量的值不包括在串行化的表示中，然而非transient型的变量是被包括进去的。
 * 注意static变量也是可以串行化的
 */
public class CollectionModifyExceptionTest {
    public static void main(String[] args) {
        Collection users = new CopyOnWriteArrayList();

        users.add(new User("张三", 28));
        users.add(new User("李四", 25));
        users.add(new User("王五", 31));
        Iterator itrUsers = users.iterator();
        while (itrUsers.hasNext()) {
            System.out.println("aaaa");
            User user = (User) itrUsers.next();
            if ("李四".equals(user.getName())) {
                users.remove(user);
                //itrUsers.remove();
            } else {
                System.out.println(user);
            }
        }
    }
}	 
