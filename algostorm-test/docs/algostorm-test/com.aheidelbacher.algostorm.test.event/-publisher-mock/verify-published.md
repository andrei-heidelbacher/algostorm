[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.event](../index.md) / [PublisherMock](index.md) / [verifyPublished](.)

# verifyPublished

`fun verifyPublished(event: Event): Unit`

Pops the event in the front of the published events queue and checks if
it is equal to the given [event](verify-published.md#com.aheidelbacher.algostorm.test.event.PublisherMock$verifyPublished(com.aheidelbacher.algostorm.event.Event)/event).

### Parameters

`event` - the expected event in the front of the published events
queue

### Exceptions

`IllegalStateException` - if the given event doesnt correspond to
the front of the queue