handleSubmit(event)
{
	event.preventDefault();
	const data = new FormData(event.target);

	fetch('/address', {
		method : 'POST',
		body : data,
	});
}
