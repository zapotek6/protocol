package uk.co.labfour.cloud2.protocol;

import uk.co.labfour.bjson.BJsonException;
import uk.co.labfour.bjson.BJsonObject;
import uk.co.labfour.error.BException;


public class BaseResponse {
	private String consumer = null;
	private String reqid = null;
	private String api = "";
	private BJsonObject payload = new BJsonObject();;
	private BJsonObject error = null;
	private int errCode = 0;
	private String errDescription = "";
	private String replyTo = null;
	
	
	public final static String kError = "error";
	public final static String kErrCode = "errCode";
	public final static String kErrDescription = "description";
	
	public BaseResponse() { }
	
	public BaseResponse(String consumer, String api, String reqid, BJsonObject payload, long ret, String replyTo) {
		this.consumer = consumer;
		this.api = api;
		this.reqid = reqid;
		this.payload = payload;
		//this.ret = ret;
		this.replyTo = replyTo;
	}

	
	public BaseResponse(BaseRequest request) {
		this.consumer = request.getConsumer();
		this.api = request.getApi();
		this.reqid = request.getReqid();
		this.replyTo = request.getReplyTo();
	}
	
	public BaseResponse setError(int errCode, String errDescription) {
		this.errCode = errCode;
		this.errDescription = errDescription;
		
		try {
			error = new BJsonObject();
			
			error.put(kErrCode, errCode);
			error.put(kErrDescription, errDescription);
		} catch (BJsonException e) {
			e.printStackTrace();
		}

		return this;
	}

	public String getReplyTo() {
		return replyTo;
	}


	public boolean containsError() {
		return (null != error);
	}
	
	public BaseResponse setReplyTo(String replyTo) {
		this.replyTo = replyTo;

		return this;
	}

	public BaseResponse removeReplyTo() {
		this.replyTo = null;

		return this;
	}

	public String getApi() {
		return api;
	}



	public BaseResponse setApi(String api) {
		this.api = api;

		return this;
	}

	public String getReqid() {
		return reqid;
	}

	public BaseResponse setReqid(String reqid) {
		this.reqid = reqid;

		return this;
	}


	public BJsonObject getPayload() {
		return payload;
	}



	public BaseResponse setPayload(BJsonObject payload) {
		this.payload = payload;

		return this;
	}

	
	
	public int getErrCode() {
		return errCode;
	}

	public String getErrDescription() {
		return errDescription;
	}

	public static BaseResponse parseJson(String input) throws BException {
		
		BaseResponse response = new BaseResponse();
		
		BJsonObject jobj = null;
		try {
			jobj = new BJsonObject(input);
			
			response.consumer = jobj.getElementAsString(BaseRequest.kConsumer);
			response.api = jobj.getElementAsString(BaseRequest.kApi);
			response.payload = getOptionalElementAsBJsonObject(jobj, BaseRequest.kPayload);
			if (jobj.has(kError)) {
				response.error = jobj.getElementAsBJsonObject(kError);
				response.errCode = response.error.getElementAsInteger(kErrCode);
				response.errDescription = response.error.getElementAsString(kErrDescription);
			}
			//if (jobj.has(kRet))  response.ret = jobj.getElementAsInteger(kRet);
			response.replyTo = getOptionalElementAsString(jobj, BaseRequest.kReplyTo);
			response.reqid = getOptionalElementAsString(jobj, BaseRequest.kReqId);
					
		} catch (BJsonException e1) {
			throw new BException(e1);
		} 
		
		return response;
		
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
	
	public static String toJsonString(BaseResponse response) throws IllegalArgumentException, BJsonException {
		
		if (null != response) {

			BJsonObject jo = new BJsonObject();
			
			if (null != response.consumer) jo.put(BaseRequest.kConsumer, response.consumer);
			if (null != response.api) jo.put(BaseRequest.kApi, response.api);
			if (null != response.reqid) jo.put(BaseRequest.kReqId, response.reqid);
			if (null != response.error) jo.put(kError, response.error);
			if (null != response.replyTo) jo.put(BaseRequest.kReplyTo, response.replyTo);
			if (null != response.payload) jo.put(BaseRequest.kPayload, response.payload);
			
			return jo.toString();
		} else return null;
		
		
		
		
	}
}
