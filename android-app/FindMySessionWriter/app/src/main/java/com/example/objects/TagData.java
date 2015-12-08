package com.example.objects;

import java.io.Serializable;

public class TagData implements Serializable {
	private long id;
	private String payload;
	private String payloadHeader;
	private String payloadType;
	private String createdAt;

	public TagData() {
		// TODO Auto-generated constructor stub
	}

	public TagData(String pl, String plh, String plt) {
		this.payload = pl;
		this.payloadHeader = plh;
		this.payloadType = plt;

	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String comment) {
		this.payload = comment;
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		return id + " - " + payload + " - " + payloadHeader + " - "
				+ payloadType + " - " + createdAt;
	}

	public String getPayloadHeader() {
		return payloadHeader;
	}

	public void setPayloadHeader(String payloadHeader) {
		this.payloadHeader = payloadHeader;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

}