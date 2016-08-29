[algostorm-test](../../index.md) / [com.aheidelbacher.algostorm.test.event](../index.md) / [PublisherMock](.)

# PublisherMock

`class PublisherMock : Publisher`

A publisher that should be used for testing purposes.

Every posted and published event is added to the back of a queue. Posted and
published events are saved in separate queues.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `PublisherMock()`<br>A publisher that should be used for testing purposes. |

### Functions

| Name | Summary |
|---|---|
| [post](post.md) | `fun <T : Event> post(event: T): Unit` |
| [publish](publish.md) | `fun <T : Event> publish(event: T): Unit` |
| [verifyEmptyPostedQueue](verify-empty-posted-queue.md) | `fun verifyEmptyPostedQueue(): Unit`<br>Checks if the posted events queue is empty. |
| [verifyEmptyPublishedQueue](verify-empty-published-queue.md) | `fun verifyEmptyPublishedQueue(): Unit`<br>Checks if the published events queue is empty. |
| [verifyPosted](verify-posted.md) | `fun verifyPosted(event: Event): Unit`<br>Pops the event in the front of the posted events queue and checks if it
is equal to the given [event](verify-posted.md#com.aheidelbacher.algostorm.test.event.PublisherMock$verifyPosted(com.aheidelbacher.algostorm.event.Event)/event). |
| [verifyPublished](verify-published.md) | `fun verifyPublished(event: Event): Unit`<br>Pops the event in the front of the published events queue and checks if
it is equal to the given [event](verify-published.md#com.aheidelbacher.algostorm.test.event.PublisherMock$verifyPublished(com.aheidelbacher.algostorm.event.Event)/event). |
