package com.wanpg.bookread.db;

public class DaoManager {

	private static DaoManager THIS = new DaoManager();
	
	private DaoManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static DaoManager getInstance(){
		return THIS;
	}
	
	private ShelfDao shelfDao;

	private DownloadDao downloadDao;
	
	private MarkerDao markerDao;
	
	public ShelfDao getShelfDao() {
		if(shelfDao==null){
			shelfDao = new ShelfDao();
		}
		return shelfDao;
	}

	public DownloadDao getDownloadDao() {
		if(downloadDao==null){
			downloadDao = new DownloadDao();
		}
		return downloadDao;
	}

	public MarkerDao getMarkerDao() {
		if(markerDao==null){
			markerDao = new MarkerDao();
		}
		return markerDao;
	}

}
