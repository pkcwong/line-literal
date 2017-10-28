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
		String[] translate = {"translate", "english", "chinese", "korean", "malaysian", "indonesian", "indo"};
		String[] review = {"review"};

		for(String keywords: timetable){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					//return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}
		for(String keywords: lift){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					//return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}
		for(String keywords: societies){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					//return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}
		for(String keywords: KMB){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					//return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}
		for(String keywords: weather){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					return new DialogFlowWeather(this).resolve().get();
				}
			}
		}
		for(String keywords: quota){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					return new CourseName(this).resolve().get();
					//return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}
		for(String keywords: anonymousChat){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					//return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}
		for(String keywords: translate){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}
		for(String keywords: review){
			String[] words = fulfillment.split("\\s+");
			for(String word: words){
				if(word.toLowerCase().equals(keywords)){
					//return new DialogFlowTranslate(this).resolve().get();
				}
			}
		}

		this.fulfillment = "Sorry, we don't understand this.";
		return this;
	}

}
