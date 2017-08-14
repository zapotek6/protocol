package uk.co.labfour.cloud2.protocol;

import java.util.UUID;

import uk.co.labfour.bjson.BJsonException;
import uk.co.labfour.bjson.BJsonObject;
import uk.co.labfour.error.BException;


public class BaseRequest {
	private String consumer = "";
	private String api = "";
	private String reqid = "";
	private BJsonObject auth = new BJsonObject();
	private BJsonObject payload = new BJsonObject();
	private String replyTo = null;
	
	private boolean anonymous;
	private boolean valid;
	public final static String kConsumer = "consumer";
	public final static String kApi = "api";
	public final static String kReqId = "reqid";
	public final static String kAuth = "auth";
	public final static String kPayload = "payload";
	public final static String kReplyTo = "replyTo";
	
	
	
	
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getConsumer() {
		return consumer;
	}
	
	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public BJsonObject getPayload() {
		return payload;
	}

	public void setConsumer(String consumer) {
		this.consumer = consumer;
	}

	public String getReqid() {
		return reqid;
	}

	public void setReqid(String reqid) {
		this.reqid = reqid;
	}

	
	public BJsonObject getAuth() {
		return auth;
	}

	public void setAuth(BJsonObject auth) {
		this.auth = auth;
	}

	public BJsonObject getPayloadAsJsonObject() {
		return payload;
	}

	public void setPayload(BJsonObject payload) {
		this.payload = payload;
	}

	
	public BJsonObject getAsBjsonObject() throws BException {
		BJsonObject obj = new BJsonObject();
		
		try {
			obj.put(kConsumer, consumer);
			obj.put(kApi, api);
			obj.put(kPayload, payload);
			obj.put(kAuth, auth);
			obj.put(kReplyTo, replyTo);
			obj.put(kReqId, reqid);			
			
		} catch (BJsonException e) {
			throw new BException("Invalid BaseRequest", e);
		}
		
		
		return obj;
	}
	
	public static BaseRequest parseJson(String input) throws BException {
		
		BaseRequest request = new BaseRequest();
		
		BJsonObject jobj = null;
		try {
			jobj = new BJsonObject(input);
						
			request.consumer = jobj.getElementAsString(kConsumer);
			request.api = jobj.getElementAsString(kApi);
			request.payload = getOptionalElementAsBJsonObject(jobj, kPayload);
			request.auth = extractAuth(jobj);
			request.replyTo = getOptionalElementAsString(jobj, kReplyTo);
			request.reqid = getOptionalElementAsString(jobj, kReqId);
					
		} catch (BJsonException e1) {
			throw new BException(e1);
		} 
		
		// TODO improve sanity check on the BaseRequest
		if (null != request.consumer) request.valid = true; else request.valid = false;

		return request;
		
	}
	
	private static BJsonObject getOptionalElementAsBJsonObject(BJsonObject obj, String elementName) {
		try {
			return obj.getElementAsBJsonObject(elementName);
		} catch (BJsonException e) {
			return null;
		}
	}
	
	private static String getOptionalElementAsString(BJsonObject obj, String elementName) {
		try {
			return obj.getElementAsString(elementName);
		} catch (BJsonException e) {
			return null;
		}
	}
	
	private static BJsonObject extractAuth(BJsonObject obj) {
		try {
			return obj.getElementAsBJsonObject(kAuth);
		} catch (BJsonException e) {
			return new BJsonObject();
		}
	}
	
	
	private static UUID parseAsUuid(String uuid) {
		try {
			return UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			return null;
		}		
	}
	
	@Override
	public String toString() {
		String output = null;
		if (null != consumer && null != reqid)
			output = "Consumer: " + consumer.toString() + " API: " + api + " REQID: " + reqid.toString();
		else if (null != consumer)
			output = "Consumer: " + consumer.toString() + " API: " + api;
		return output;
	}
}
