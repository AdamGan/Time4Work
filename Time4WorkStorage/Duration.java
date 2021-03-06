package Time4WorkStorage;

import Time4WorkStorage.Tasks.TaskType;

public class Duration {
	
	private String startDate = "";		//date format is �DDMMYY�
	private String startTime = "";		//time is 24hrs �0000�
	private String endDate = "";
	private String endTime = "";
	private int type = 0;				
	private static final int DeadlineType = TaskType.DeadlineType.getTaskType();
	private static final int DurationType = TaskType.DurationType.getTaskType();

	public Duration(String sDate, String sTime, String eDate, String eTime) {
		setStartDate(sDate);
		setStartTime(sTime);
		setEndDate(eDate);
		setEndTime(eTime);
		setType(DurationType);
	}
	
	public Duration(String eDate, String eTime) {
		setEndDate(eDate);
		setEndTime(eTime);
		setType(DeadlineType);
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
