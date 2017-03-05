$( document ).ready(function(){
	console.log("READY");
	
	
    
	 $("#getCostDetailsBtnId").click(function(){
 		$("#displayDiv #resultRow").html("");
 		$("#displayDiv").slideUp(1000);
 		$.post("costPrediction.htm",$("#formDetailId").serialize(),function(response){displayTheContent(response);});
 	});
	
	function displayTheContent(response){

		var row = response.split("#@");

		var obj = JSON.parse(row[0]);
		var obj2 = JSON.parse(row[1]);
		var size=obj.Results.output1.value.Values.length;

				var pickup= $("#us3-address").val();
				var dropoff=$("#us1-address").val();
				var datetime=$("#datetimepicker").val();
				var price=Number(obj.Results.output1.value.Values[0][10]).toFixed(2);
				var surcharge=Number(obj2.Results.output1.value.Values[0][10]).toFixed(2);
				if(surcharge <= 0){
					surcharge = "No Surcharge";
				}
				surcharge = surcharge + " $"
				price = price + " $"
				var prevHtml=$("#displayDiv #resultRow").html();
				$("#displayDiv #resultRow").html(prevHtml+"<tr> <td>"+pickup+"</td><td>"+dropoff+"</td><td>"+datetime+"</td><td>"+price+"</td><td>"+surcharge+"</td>");
		

		$("#displayDiv").slideDown(1000).css({'display' : 'block'});
	}

})
	

	