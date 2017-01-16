// Please add the email address "mrpotatohead@10gen.com" to the list of addresses
// in the "headers.To" array for the document with "headers.Message-ID" of
// "<8147308.1075851042335.JavaMail.evans@thyme>"

db.messages.update(
	{"headers.Message-ID" : "<8147308.1075851042335.JavaMail.evans@thyme>"}
	,{$push : {"headers.To" : "mrpotatohead@10gen.com"}}
	,false
	,false
)