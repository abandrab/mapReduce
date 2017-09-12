package models;

public class Doc 
{
	private String documentId;
	private int count;
	
	public Doc(String documentId, int count)
	{
		this.setDocumentId(documentId);
		this.setCount(count);
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
