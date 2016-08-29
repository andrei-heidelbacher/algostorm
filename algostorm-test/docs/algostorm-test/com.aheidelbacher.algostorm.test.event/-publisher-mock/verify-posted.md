[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.event](../index.md) / [PublisherMock](index.md) / [verifyPosted](.)

# verifyPosted

`fun verifyPosted(event: Event): Unit`

Pops the event in the front of the posted events queue and checks if it
is equal to the given [event](verify-posted.md#com.aheidelbacher.algostorm.test.event.PublisherMock$verifyPosted(com.aheidelbacher.algostorm.event.Event)/event).

### Parameters

`event` - the expected event in the front of the posted events queue

### Exceptions

`IllegalStateException` - if the given event doesnt correspond to
the front of the queue