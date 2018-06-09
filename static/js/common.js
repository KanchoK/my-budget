const d = new Date();

const common = {
    getMonthString: function () {
        const m = d.getMonth() + 1;
        const mStr = m < 10 ? '0' + m.toString() : m.toString();
        return mStr + '-' + d.getFullYear();
    },
    getDate: function () {
        const date = d.getDate();
        const dateStr = date < 10 ? '0' + date.toString() : date.toString();
        return dateStr + '-' + this.getMonthString();
    }
};

module.exports = common;