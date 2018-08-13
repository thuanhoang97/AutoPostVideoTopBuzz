package topbuzz.model.videosource;

public class VideoFile {
	private String absolutePath;
	private String title;
	
	public VideoFile() {
		this("","");
	}
	
	public VideoFile(String absolutePath) {
		this(absolutePath, "");
	}
	
	public VideoFile(String absolutePath, String fileName) {
		this.absolutePath = absolutePath;
		this.title = getTitleByName(fileName);
	}
	
	private String getTitleByName(String fileName) {
		int dot = fileName.lastIndexOf(".");
		return fileName.substring(0, dot);
	}
	
	
	public String getAbsolutePath() {return absolutePath;}
	public String getTitle() {return title;}
	public void setTitle(String title) {this.title = title;}
	
}
