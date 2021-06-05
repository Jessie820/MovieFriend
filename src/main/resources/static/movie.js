function popup(movie){
         alert("테스트"+movie.title);
         alert("테스트"+movie.link);

         $.ajax({
            type: 'POST',
            url: '/movies/save',
            data: JSON.stringify(movie),
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
            }).done(function(res) {
                alert("추천 영화로 등록되었습니다.");
            }).fail(function (error) {
            alert(JSON.stringify(error));
            });

}

$(document).ready(
		function() {
            $('#btn-movies-search').on('click', function () {
                alert("누름");
                var keyword = $('#keyword').val();
                       $.ajax({
                           type: 'GET',
                           url: '/movies/'+keyword
                           //dataType: 'json'
                           //contentType:'application/json; charset=utf-8',
                           }).done(function(res) {
                           $("#list").replaceWith(res);
                          // alert(JSON.stringify(res));
                           }).fail(function (error) {
                           alert(JSON.stringify(error));
                           });
            });

            $('#btn-movies-save').on('click', function () {
                alert("누름");
                $("input[name='checkedOne']:checked").each(function(i){
                    alert($(this).title);
                });
               //alert("keyword값: " + keyword);
           /*    $.ajax({
                   type: 'POST',
                   url: '/movies/save'
                   //dataType: 'json'
                   //contentType:'application/json; charset=utf-8',
                   }).done(function(res) {

                  // alert(JSON.stringify(res));
                   }).fail(function (error) {
                   alert(JSON.stringify(error));
                   });*/
             });

		})

