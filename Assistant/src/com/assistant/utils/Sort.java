package com.assistant.utils;



import java.util.ArrayList;
import com.assistant.model.ConsumerModel;

import net.sourceforge.pinyin4j.PinyinHelper;

public class Sort {
	public String getPinYinHeadChar(String str) {
		String convert = "";
			char word = str.charAt(0);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null)
				convert = String.valueOf(pinyinArray[0].charAt(0));
			else {
				convert = String.valueOf(word);
				}
		return convert;
	}
	public String[] autoSort(String[] strArr) {
		String temp = "";
		String headchar1;
		String headchar2;
		for (int i = 0; i < strArr.length; i++) {
			for (int j = i; j < strArr.length; j++) {
				headchar1 = getPinYinHeadChar(strArr[i]).toUpperCase();
				headchar2 = getPinYinHeadChar(strArr[j]).toUpperCase();
				if (headchar1.charAt(0) > headchar2.charAt(0)) {
					temp = strArr[i];
					strArr[i] = strArr[j];
					strArr[j] = temp;
				}
			}
		}
		return strArr;
	}
	public ArrayList<ConsumerModel> autoSort(ArrayList<ConsumerModel> songModel) {
		ConsumerModel userInfoTemp_1 = null;
		ConsumerModel userInfoTemp_2 = null;
		String headchar1;
		String headchar2;

		if (songModel != null && songModel.size() > 1)
			for (int i = 0; i < songModel.size(); i++) {
				for (int j = i; j < songModel.size(); j++) {
					headchar1 = getPinYinHeadChar(songModel.get(i).getCustomer_name())
							.toUpperCase();
					headchar2 = getPinYinHeadChar(songModel.get(j).getCustomer_name())
							.toUpperCase();
					if (headchar1.charAt(0) > headchar2.charAt(0)) {
						userInfoTemp_1 = songModel.get(i);
						userInfoTemp_2 = songModel.get(j);
						userInfoTemp_1.setPinyin(headchar1);
						userInfoTemp_2.setPinyin(headchar2);
						songModel.set(i, userInfoTemp_2);
						songModel.set(j, userInfoTemp_1);
					}
				}
			}
		else if(songModel.size() == 1)
			songModel.get(0).setPinyin(getPinYinHeadChar(songModel.get(0).getCustomer_name()).toUpperCase());
		return songModel;
	}
	public String getAllPinYinHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}
}
