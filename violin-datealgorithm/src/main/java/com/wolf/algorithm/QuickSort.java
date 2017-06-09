package com.wolf.algorithm;

/**
 * Description:
 * 平均时间复杂度：nlogn
 * 空间复杂度：logn
 * 不稳定
 * 27 25 27 3，已第一个27为中心，将3和第一个27交换得到  3 25 27 27，这个不稳定了
 * <br/> Created on 2017/5/2 18:22
 *
 * @author 李超
 * @since 1.0.0
 */
public class QuickSort {

    //网上其他答案
    public void quickSort(int[] strDate, int left, int right) {
        int middle, tempDate;
        int i = left;
        int j = right;
        middle = strDate[(i + j) / 2];
        do {
            while(strDate[i] < middle && i < right) {//找出左边比中间值大于等于的数
                i++;
            }
            while(strDate[j] > middle && j > left) {//找出右边比中间值小于等于的数
                j--;
            }
            if(i <= j) { //将左边大的数和右边小的数进行替换
                tempDate = strDate[i];
                strDate[i] = strDate[j];
                strDate[j] = tempDate;
                i++;
                j--;
            }
        } while(i <= j); //当两者交错时停止

        if(i < right) {
            quickSort(strDate, i, right);//排序右边
        }
        if(j > left) {
            quickSort(strDate, left, j);//排序左边
        }
    }

    //第一版,填上挖坑
    public void quickSort1(int[] strDate, int left, int right) {

//        if(left > right) {等于是没有意义的
        if(left >= right) {
            return;
        }

        int i = left;
        int j = right;
        int temp = strDate[left];
        while(i < j) {

//            while(true){这里也需要判断一下i和j
            while(i < j) {
                if(strDate[j] < temp) {
                    strDate[i] = strDate[j];
                    i++;
                    break;
                }
                j--;
            }

//            while(true){
            while(i < j) {
                if(strDate[i] > temp) {
                    strDate[j] = strDate[i];
                    j--;
                    break;
                }
                i++;
            }
        }

        strDate[i] = temp;

        quickSort1(strDate, left, i - 1);
        quickSort1(strDate, i + 1, right);
    }

    //第二版,填上挖坑
    public void quickSort2(int[] strDate, int left, int right) {

        if(left >= right) {
            return;
        }

        int i = adjustArray(strDate, left, right);
        quickSort2(strDate, left, i - 1);
        quickSort2(strDate, i + 1, right);
    }

    //调整数组后，中间的值处于正确位置，左边的数小于中间值，右边的数大于中间值
    private int adjustArray(int[] strDate, int left, int right) {
        int i = left;
        int j = right;
        int temp = strDate[left];
        while(i < j) {

            //从右找第一个比temp小的
//            while(i < j && strDate[j] > temp) {漏掉了一个等于
            while(i < j && strDate[j] >= temp) {
                j--;
            }
            if(i < j) {//上面能保证到这里是i<=j，但是i=j后又赋值就不对了
//                strDate[i] = strDate[j];合并成一条
//                i++;
                strDate[i++] = strDate[j];
            }

            //从左找第一个比temp大的
//            while(i < j && strDate[i] < temp) {漏掉了一个等于
            while(i < j && strDate[i] <= temp) {
                i++;
            }
            if(i < j) {
//                strDate[j] = strDate[i];合并成一条
//                j--;
                strDate[j--] = strDate[i];
            }
        }

        strDate[i] = temp;
        return i;
    }

    /**
     *   * @param args
     *  
     */
    public static void main(String[] args) {
        int[] strVoid = new int[]{6, 8, 5, 9, 3, 4, 7};
        QuickSort sort = new QuickSort();
        sort.quickSort(strVoid, 0, strVoid.length - 1);
        for(int i = 0; i < strVoid.length; i++) {
            System.out.println(strVoid[i] + " ");
        }
    }
}
