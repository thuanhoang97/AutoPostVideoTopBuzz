package topbuzz.model.videosource;

import java.io.File;
import java.util.ArrayList;

public class VideoSource {
	private ArrayList<VideoFile> videoFiles;
	
	public VideoSource() {
		
	}
	
	public void getFromForder(String forderPath) {
		videoFiles = new ArrayList<>();
		File forder = new File(forderPath);
		for(File file : forder.listFiles()) {
			videoFiles.add(new VideoFile(file.getAbsolutePath(), file.getName()));
		}
	}
	
//	public void sortByName() {
//		int n = videoFiles.size();
//		for(int i=0; i<n; i++) {
//			for(int j=n-1; j>i; j--) {
//				if(videoFiles.)
//			}
//		}
//	}
	
	public String toString() {
		String s = "";
		for(int i=0; i<videoFiles.size(); i++) {
			s += (i+1)+ ". " + videoFiles.get(i).getTitle() + "\n";
		}
		return s;
	}
	
	public ArrayList<VideoFile> getFiles() {
		return videoFiles;
	}
}
