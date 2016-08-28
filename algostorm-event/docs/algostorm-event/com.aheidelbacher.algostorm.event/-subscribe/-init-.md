[algostorm-event](../../index.md) / [com.aheidelbacher.algostorm.event](../index.md) / [Subscribe](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`Subscribe()`

Annotation to mark a method of a [Subscriber](../-subscriber.md) as an event handler.

An event handler is a method that is annotated with the [Subscribe](index.md)
annotation, is public, final, non-static, returns [Unit](#) / void and receives
a single parameter which is a subtype of [Event](../-event.md).

Event-handling methods declared in supertypes are also registered for events
upon subscription.

