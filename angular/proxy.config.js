module.exports = [
    {
        context: [ '/api/**' ],
        // * match only 1 level, ** match recursively
        target: 'http://localhost:8080', //SpringBoot!
        secure: false,
    }
]