package com.wanpg.bookread.manager;

import java.util.List;
import java.util.Map;

public class ShelfManager {
	private static ShelfManager THIS = new ShelfManager();
	
	private ShelfManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static ShelfManager getManager(){
		return THIS;
	}
	
    public final static int CATALOG_REQUEST_CODE = 1;

    public final static int CATALOG_RESULT_CODE = 2;

    /**
     * 书城的分类
     */
    public Map<String, List<Map<String, Object>>> mapCategory = null;
//	public int addNewBookToBookShelf(ShelfBook shelfBookMode) {
//		boolean isAdd = false;
//		boolean isUpdate = false;
//		int newInShelfPos = -1;
//		String readMode = shelfBookMode.readMode;
//		if (readMode.equals(ShelfBook.POS_LOCAL_SDCARD)) {
//			if (FileUtil.checkLocalFileIsCouldOpen(new File(shelfBookMode.bookPath).getName())) {
//				for (int i = 0; i < listLocalShelfBookModes.size(); i++) {
//					if (listLocalShelfBookModes.get(i).bookPath.equals(shelfBookMode.bookPath)) {
//						newInShelfPos = i;
//						break;
//					}
//				}
//				if (newInShelfPos == -1) {
//					newInShelfPos = listLocalShelfBookModes.size();
//					listLocalShelfBookModes.add(shelfBookMode);
//					isAdd = true;
//				}
//			} else {
//				Notice.showToast("抱歉，目前不支持此种格式的书籍！");
//				return -1;
//			}
//		} else if (readMode.equals(ShelfBook.POS_LOCAL_SHUPENG)) {
//			for (int i = 0; i < listLocalShelfBookModes.size(); i++) {
//				if (listLocalShelfBookModes.get(i).bookPath.equals(shelfBookMode.bookPath)) {
//					newInShelfPos = i;
//					break;
//				}
//			}
//			if (newInShelfPos == -1) {
//				newInShelfPos = listLocalShelfBookModes.size();
//				listLocalShelfBookModes.add(shelfBookMode);
//				isAdd = true;
//			}
//		} else if (readMode.equals(ShelfBook.POS_ONLINE)) {
//			for (int i = 0; i < listLocalShelfBookModes.size(); i++) {
//				if (listLocalShelfBookModes.get(i).url.equals(shelfBookMode.url)) {
//					newInShelfPos = i;
//					if (listLocalShelfBookModes.get(i).chapterId != shelfBookMode.chapterId) {
//						listLocalShelfBookModes.remove(newInShelfPos);
//						listLocalShelfBookModes.add(newInShelfPos, shelfBookMode);
//						isUpdate = true;
//					}
//					break;
//				}
//			}
//
//			if (newInShelfPos == -1) {
//				newInShelfPos = listLocalShelfBookModes.size();
//				listLocalShelfBookModes.add(shelfBookMode);
//				isAdd = true;
//			}
//		}
//
//		if (isAdd) {
//			isShelfChanged = true;
//			ShelfDao dao = DaoManager.getInstance().getShelfDao();
//			dao.delLocalShelfBookMode(shelfBookMode);
//			dao.addNewLocalShelfBookMode(shelfBookMode);
//		}
//
//		if (isUpdate) {
//			isShelfChanged = true;
//			ShelfDao dao = DaoManager.getInstance().getShelfDao();
//			dao.updateLocalShelfBookMode(shelfBookMode, true);
//		}
//		return newInShelfPos;
//	}
}
