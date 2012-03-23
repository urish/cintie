$(document).ready(function(){
	var fieldSize = {
		width: $('.playground-board').width() - $('.pawn-1').width(),
		height: $('.playground-board').height() - $('.pawn-1').height(),
	};
	
	function updatePawns() {
		$.getJSON("/app/pawns", function(data) {
			data.pawns.map(function(pawn) {
				$(".pawn-" + pawn.id).show().css("left", pawn.x * fieldSize.width)
					.css("top", pawn.y * fieldSize.height);
			});
		});
	}
	
	$("#btn-play").click(function(event) {  
		$.ajax({url: "/app/start"});  
		updatePawns();
		event.stopPropagation();
	});
	
	$("#btn-stop").click(function(event) {  
		$.ajax({url: "/app/stop"});  
		event.stopPropagation();
	});
	
	function movePawn(name, position) {
		var pawnId = name.replace(/[^\d]/g, '');
		var left = position.left / fieldSize.width;
		var top = position.top / fieldSize.height;
		$.ajax({
			type: "POST",
			url: "/app/pawns/" + pawnId, 
			data: {
				x: left,
				y: top
			}
		});  
	}
	
	$(".pawn").hide().draggable({
		containment: 'parent',
		drag: function(event, ui) {
			movePawn(this.className, ui.position)
		},
		stop: function(event, ui) {
			movePawn(this.className, ui.position)
		}
	});
	
	updatePawns();
});
