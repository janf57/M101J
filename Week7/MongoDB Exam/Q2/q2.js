// Please use the Enron dataset you imported for the previous problem. For this
// question you will use the aggregation framework to figure out pairs of people
// that tend to communicate a lot. To do this, you will need to unwind the To
// list for each message.

// This problem is a little tricky because a recipient may appear more than once
// in the To list for a message. You will need to fix that in a stage of the
// aggregation before doing your grouping and counting of (sender, recipient)
// pairs.

db.messages.aggregate([
	{$project:
		{
			_id:1 
			,From:"$headers.From"
			,To: "$headers.To"
		}
	}
	,{$unwind:"$To"}
	,{$group:
		{
			_id: {From:"$From", id:"$_id"}
			,To: {$push:"$To"}
		}
	}
	,{$unwind:"$To"}
	,{$group:
		{
			_id: {from:"$_id.From", to:"$To"}
			,count: {$sum:1}
		}
	}
	,{$sort:{count:-1}}	
	,{$limit:3}
])

