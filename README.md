# coursera-notes

work in progress

Send your coursera lecture notes to readwise. 

example: 
```bash
sbt> run "--courseId" "SgcGB0YOEeemlQrrzf9X-A" "--courseUrl" "https://www.coursera.org/learn/financial-markets-global/" "--courseTitle" "Financial Markets" "--courseAuthor" "Robert Shiller" --courseraCookie <courseraCookie> "--readwiseToken" <readwiseToken>
```

- `courseId`: inspect some of the requests to see this value: TODO
- `courseraCookie`: inspect your browser requests against coursera to get this value
- `readwiseToken`: get yours [here](https://readwise.io/access_token)