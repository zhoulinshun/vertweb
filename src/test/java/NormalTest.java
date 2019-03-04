import cn.miss.vert.GraphqlVerticle;
import org.junit.Test;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * @Author: zhoulinshun
 * @Description:
 * @Date: Created in 2019-02-27 18:47
 */
public class NormalTest {

    @org.junit.Test
    public void test() throws IOException {
        ClassRelativeResourceLoader classRelativeResourceLoader = new ClassRelativeResourceLoader(this.getClass());
        Resource resource = classRelativeResourceLoader.getResource("/");
        URI uri = resource.getURI();
        System.out.println(uri);
    }

    @Test
    public void test2() {
        URL resource = GraphqlVerticle.class.getResource("/");
        System.out.println(resource.getPath());
    }

    @Test
    public void leet() {
        ListNode make = make(new int[]{1, 2, 3, 4, 12, 18});
//        ListNode make1 = make(new int[]{4, 7, 9});
//        ListNode listNode = mergeKLists(new ListNode[]{make, make1});
//        System.out.println(listNode);
        ListNode listNode = reverseKGroup(make, 7);
        System.out.println(listNode.toString());
//        System.out.println(reg("mississippi", "mis*is*p*."));
    }

    public ListNode make(int[] values) {
        ListNode head = new ListNode(values[0]);
        ListNode temp = head;
        for (int i = 1; i < values.length; i++) {
            temp.next = new ListNode(values[i]);
            temp = temp.next;
        }

        return head;
    }

    public boolean match(String s, String pattern) {
        char[] chars = s.toCharArray();
        char[] patternC = pattern.toCharArray();

        int index = 0;

        for (int i = 0; i < patternC.length; i++) {
            char c = patternC[i];
            if (c == '.') {
                index++;
            } else if (c == '*') {
                if (i == 0) {
                    index++;
                } else {

                }
            }

        }

        int index2 = s.length() - 1;
        for (int i = patternC.length - 1; i >= 0; i--) {

        }

        return true;
    }

    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null) {
            return head;
        }
        ListNode tempHead = head;
        for (int i = 1; i < k; i++) {
            ListNode currentNode = head.next;
            if (currentNode != null) {
                head.next = currentNode.next;
                currentNode.next = tempHead;
                tempHead = currentNode;
            }
        }
        return tempHead;
    }

    public ListNode mergeKLists(ListNode[] listNodes) {
        ListNode head = null;
        ListNode temp = head;
        while (true) {
            int min = -1;
            for (int i = 0; i < listNodes.length; i++) {
                ListNode listNode = listNodes[i];
                if (listNode != null) {
                    if (min == -1) {
                        min = i;
                    } else {
                        if (listNodes[min].val > listNode.val) {
                            min = i;
                        }
                    }
                }
            }
            if (min < 0) {
                break;
            } else {
                ListNode listNode = new ListNode(listNodes[min].val);
                listNodes[min] = listNodes[min].next;
                if (head == null) {
                    head = listNode;
                    temp = head;
                } else {
                    temp.next = listNode;
                    temp = temp.next;
                }
            }
        }
        return head;
    }

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) { val = x; }

        @Override
        public String toString() {
            ListNode current = this;
            StringBuilder sb = new StringBuilder("begin");
            while (current != null) {
                sb.append("->").append(current.val);
                current = current.next;

            }
            return sb.toString();
        }
    }

    public boolean reg(String s, String pattern) {
        return Pattern.matches(pattern, s);
    }
}
