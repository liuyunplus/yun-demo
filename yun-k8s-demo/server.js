let express = require('express')
const fs = require('fs');
const path = require('path');

let app = express()

app.get('/',(req,res) => res.end('hello world'))
app.get('/test',(req,res) => res.end('test liuyun'))

app.get('/files', function(req, res, next) {
    // 显示服务器文件
    // 文件目录
    var filePath = path.join(__dirname, './');
    fs.readdir(filePath, function(err, results){
        if(err) throw err;
        if(results.length>0) {
            var files = [];
            results.forEach(function(file){
                if(fs.statSync(path.join(filePath, file)).isFile()){
                    files.push(file);
                }
            })
            res.json(files);
        } else {
            res.end('当前目录下没有文件');
        }
    });
});

app.get('/file/:fileName', function(req, res, next) {
    // 实现文件下载
    var fileName = req.params.fileName;
    var filePath = path.join(__dirname, fileName);
    var stats = fs.statSync(filePath);
    if(stats.isFile()){
        res.set({
            'Content-Type': 'application/octet-stream',
            'Content-Disposition': 'attachment; filename='+fileName,
            'Content-Length': stats.size
        });
        fs.createReadStream(filePath).pipe(res);
    } else {
        res.end(404);
    }
});


app.listen(8080,() => console.log('Server is running...'))