const d = new Date();

const common = {
    getMonthString: function () {
        const m = d.getMonth() + 1;
        const mStr = m < 10 ? '0' + m.toString() : m.toString();
        return mStr + '-' + d.getFullYear();
    }
};

module.exports = common;