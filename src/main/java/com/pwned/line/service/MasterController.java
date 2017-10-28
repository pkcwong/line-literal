package com.pwned.line.service;

/***
 * Master Controller for Service modules.
 * Required params: []
 * Reserved tokens: []
 * Resolved params: []
 * @author Calvin Ku
 */
public class MasterController extends DefaultService {

	public MasterController(Service service) {
		super(service);
	}


	@Override
	public void payload() throws Exception {

	}

	/***
	 * Chaining to specific service modules.
	 * @return Service
	 * @throws Exception Exception
	 */
	@Override
	public Service chain() throws Exception {
		String[] timetable = {"current"};
		String[] lift = {"classroom", "room", "lift"};
		String[] societies = {"societies"};
		String[] KMB = {"bus", "arrival", "departure"};
		String[] weather = {"weather"};
		String[] quota = {"COMP", "ENGG", "class"};
		String[] anonymousChat = {"chat"};
		String[] translate = {"translate"};
		String[] review = {"review"};

		for(int i=0; i<timetable.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<lift.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<societies.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<KMB.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<weather.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<quota.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				return new CourseName(this).resolve().get();
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<anonymousChat.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<translate.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				return new DialogFlowTranslate(this).resolve().get();
			}
		}
		for(int i=0; i<review.length; i++){
			String[] keywords = fulfillment.split("\\s+");
			if(keywords[i].toLowerCase() == timetable[i]){
				//return new DialogFlowTranslate(this).resolve().get();
			}
		}


		this.fulfillment = "Sorry, we don't understand this.";
	}

}
