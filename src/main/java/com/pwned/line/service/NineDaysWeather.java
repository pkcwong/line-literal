package com.pwned.line.service;

import com.pwned.line.job.PushNineDaysWeather;

/***
 * Service for course information.
 * Required params: [uid]
 * Reserved tokens: [@weather::ninedaysweather]
 * Resolved params: []
 * @author Timothy Pak
 */

public class NineDaysWeather extends DefaultService{


    public NineDaysWeather(Service service) {
        super(service);
    }
    @Override
    public void payload() throws Exception {
//        String link = "http://www.weather.gov.hk/wxinfo/currwx/fnd.htm";
//        String[] text = {"http://www.weather.gov.hk", "<img border=\"0\" src=\"", "text-align:left;padding: 0px;font-size:100%;", "</div>"};
//        HTTP http = new HTTP(link);
//        String ninedaysweather = http.get();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd E");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
//        Date today = new Date();
//        String[] date = new String[9];
//        Calendar c = Calendar.getInstance();
//        c.setTime(today);
//        for(int dd = 0; dd < 9; dd++){
//            c.add(Calendar.DATE, 1);
//            Date nextday = c.getTime();
//            date[dd] = dateFormat.format(nextday);
//        }
//        String imageurlstring = ninedaysweather;
//        String[] imageurl = new String[9];
//        for (int url = 0; url < 9; url++){
//            imageurlstring = imageurlstring.substring(imageurlstring.indexOf(text[1]));
//            imageurl[url] = text[0] + imageurlstring.substring(imageurlstring.indexOf("/"), imageurlstring.indexOf("/") + 24);
//            imageurlstring = imageurlstring.substring(2);
//        }
//        String desriptionsstring = ninedaysweather;
//        String[] desription = new String[9];
//        for(int weather = 0; weather < 9; weather++){
//            desriptionsstring = desriptionsstring.substring(desriptionsstring.indexOf(text[2]));
//            desription[weather] = desriptionsstring.substring(text[2].length() + 4, desriptionsstring.indexOf(text[3]));
//            desriptionsstring = desriptionsstring.substring(2);
//        }
//        for(int i = 0; i < 20; i++)
//            System.out.println("|||Carousel|||");
//        ArrayList<CarouselColumn> nineColumns = new ArrayList<>();
//        for(int days = 0; days < 5; days++){
//            nineColumns.add(days, new CarouselColumn(imageurl[days], date[days], desription[days], null));
//            System.out.println("||||||" + days + "||||||");
//        }
//        for(int i = 0; i < 20; i++)
//        System.out.println(nineColumns);
        //confirmTemplate = new ConfirmTemplate("Anonymous Chat System", new PostbackAction("terminate", "anonymous::terminate"), new PostbackAction("cancel", "anonymous::cancel"));
//        CarouselTemplate carouselTemplate = new CarouselTemplate(nineColumns);
//        this.fulfillment = this.fulfillment.replace("@weather::ninedaysweather", "The following is the weather forecast for next 9 days:");
//        KitchenSinkController.push(this.getParam("uid").toString(), new TemplateMessage("Nine Days Weather Forecast", carouselTemplate));

        PushNineDaysWeather.NineDaysWeather(this.getParam("uid").toString());
    }


    @Override
    public Service chain() throws Exception {
        return this;
    }
}
