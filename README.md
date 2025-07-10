# notification service 
this project is our notification system. it handles sending emails, sms messages, and push notifications. the goal was to build something reliable and flexible so we can easily swap out external services if needed.

## overview
from the start, i wanted something robust and flexible. that's why i went for a hexagonal architecture approach. imagine it like this:

the core brain: this is where our actual notification logic lives. it knows what a notification is, how to decide if it should be sent, and what it should look like. it doesn't care how it gets sent or where the data comes from. it just focuses on the business of notifying.

the action plans: this layer takes requests (like "send a welcome email") and orchestrates the steps. it talks to the brain for decisions and then asks the outside world(specific connections in this case) to do the actual sending or saving.

the outside connections: these are the parts that actually plug into real-world services like databases, email senders, sms provider, or push notification services. the cool thing is, our brain and action plans don't know or care about the specifics of these, they just use the 'connections'. this means we can swap out one service for another service without touching our core logic.

the idea was to practice the concept of DDD, whilst introducing a bit of complexity whilst applying it. for this use case, the separation keeps everything tidy and makes it much easier to test and adapt.

## architecture
![Notification Service Architecture](design.png)