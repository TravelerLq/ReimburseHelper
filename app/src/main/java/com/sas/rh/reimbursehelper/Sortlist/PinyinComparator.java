package com.sas.rh.reimbursehelper.Sortlist;

import com.sas.rh.reimbursehelper.Bean.MemberDetailInfoEntity;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<MemberDetailInfoEntity> {

	public int compare(MemberDetailInfoEntity o1, MemberDetailInfoEntity o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
