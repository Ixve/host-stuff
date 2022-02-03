const generateRandom = (length: number): string => {
    const characters: string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmno[qrstuvwxyz1234567890';
    const charactersLength: number = characters.length;

    var result: string = '';

    for (var i: number = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    return result;
};

export { generateRandom };