function timer()
{
    var endDate = new Date("December 12, 2018");
    var nowTime = new Date();
    var time = endDate - nowTime;
    var s = time.toString();
    return (s);
}

function dateTimer()
{
    var date = timer();

    if (date <= 0)
    {
        return "Delayed Release"
    }
    else
    {
        //code taken from w3 school
        // Time calculations for days, hours, minutes and seconds
        var days = Math.floor(date / (1000 * 60 * 60 * 24));
        var hours = Math.floor((date % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((date % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((date % (1000 * 60)) / 1000);
        //https://www.w3schools.com/howto/howto_js_countdown.asp

        var completeDate = days + " D " + hours + " H " + minutes + " M " + seconds + " S";
        return completeDate;
    }
}

function test1()
{
    var id = document.querySelector("timer");
    setInterval()
    document.getElementById("timer").innerHTML = test();
}

function test()
{
    var endDate = new Date("December 25, 2018");
    var nowTime = new Date();
    var time = endDate - nowTime;
    var s = time.toString();
    return (s);
}
