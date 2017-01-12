package com.qe.qzin.models;
import java.util.ArrayList;
import java.util.Date;

public class Event {

  String eventTitle;
  String eventLocation;
  Date   eventDate;
  String eventDescr;
  double eventPrice;


  public Event(String title, String location, Date date, String descr, double price)
  {
    this.eventTitle = title;
    this.eventLocation = location;
    this.eventDate = date;
    this.eventDescr = descr;
    this.eventPrice = price;
  }

  public String getEventTitle() {
    return eventTitle;
  }

  public String getEventLocation() {
    return eventLocation;
  }

  public Date getEventDate() {
    return eventDate;
  }

  public String getEventDescr() {
    return eventDescr;
  }

  public double getEventPrice() {
    return eventPrice;
  }

 public static ArrayList<Event> createEventList(int count)
 {
   ArrayList<Event> events = new ArrayList<Event>();

   for(int i=0; i< count; i++)
   {
     events.add(new Event("Bombay Snaks","Pleasonton,CA",new Date(),"Enjoy Bombay style food",20));
     events.add(new Event("American Burger Party","San Jose,CA",new Date(),"Burgers American Style!",15));
   }
   return  events;
 }

}
