package org.yuhao.springcloud.common.util.range;

import java.sql.Time;
import java.util.*;

public class Main {

    static class TimeRange {
        int start;
        int end;

        public TimeRange(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "TimeRange{" +
                    "start=" + start +
                    ", end=" + end +
                    '}';
        }
    }

    private List<TimeRange> list;

    public Main(TimeRange... ranges) {
        list = new ArrayList<>(Arrays.asList(ranges));
        List<TimeRange> temp2 = new ArrayList<>();
        Iterator<TimeRange> iterator = list.iterator();
        while (iterator.hasNext()) {
            TimeRange next = iterator.next();
            if (next.end < next.start) {
                temp2.add(new TimeRange(next.start, 24));
                temp2.add(new TimeRange(0, next.end));
                iterator.remove();
            }
        }
        list.addAll(temp2);
        list.sort(Comparator.comparingInt(o -> o.start));
    }

    public void merge() {
        Iterator<TimeRange> iterator1 = list.iterator();
        TimeRange next = iterator1.next();
        while (iterator1.hasNext()) {
            TimeRange next1 = iterator1.next();
            if (next1.start <= next.end) {
                next.end = next1.end;
                iterator1.remove();
            } else {
                next = next1;
            }
        }
    }

    public int findSmaller(int time) {
        int start = 0, end = list.size() - 1;
        while (start <= end) {
            int mid = (start + end) / 2;
            TimeRange timeRange = list.get(mid);
            if (timeRange.start == time) {
                return mid;
            } else if (timeRange.start > time) {
                end = mid - 1;
            } else if (mid == list.size() - 1 || list.get(mid + 1).start > time) {
                return mid;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }

    public TimeRange find(int time) {
        int index = findSmaller(time);
        if (index == -1) {
            return null;
        }
        if (list.get(index).end >= time) {
            return list.get(index);
        }
        return null;
    }


    public void add(TimeRange range) {
        int index = findSmaller(range.start);
        if (index == -1) {
            list.add(0, range);
        } else {
            list.add(index + 1, range);
        }
        merge();
    }

    public static void main(String[] args) {
        Main main = new Main(new TimeRange(2, 3), new TimeRange(2, 5), new TimeRange(4, 11), new TimeRange(12, 13), new TimeRange(17, 1));
        main.merge();
//        System.out.println(main.list);
        System.out.println(main.find(15));
        main.add(new TimeRange(14, 16));
        System.out.println(main.find(15));
        ArrayList<Object> objects = new ArrayList<>();
    }
}
