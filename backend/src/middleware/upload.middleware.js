const multer = require('multer');
const { GridFsStorage } = require('multer-gridfs-storage');

const storage = new GridFsStorage({
    url: process.env.MONGO_URI,
    options: { useNewUrlParser: true, useUnifiedTopology: true },
    file: (req, file) => {
        return {
            bucketName: 'uploads', // collection name
            filename: `${Date.now()}-${file.originalname}`
        };
    }
});

const upload = multer({ storage });

module.exports = upload;
