var fs = require('fs'),
    S3FS = require('s3fs'),
    s3fsImpl = new S3FS('mybucketname', {
        accessKeyId: "AKIAJGOMVHDHEA3S2ISA",
        secretAccessKey: "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"
    });

// Create our bucket if it doesn't exist
s3fsImpl.create();

exports.upload = function (req, res) {
    var file = req.files.file;
    var stream = fs.createReadStream(file.path);
    return s3fsImpl.writeFile(file.originalFilename, stream).then(function () {
        fs.unlink(file.path, function (err) {
            if (err) {
                console.error(err);
            }
        });
        res.status(200).end();
    });
};