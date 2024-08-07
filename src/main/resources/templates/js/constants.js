
const URL_LOGIN = '/login'

const URL_API_AUTH = '/auth'
const URL_API_REGISTER = '/register'
const URL_API_ME = '/me'
const URL_API_UPDATE_TOKEN = '/token'
const URL_API_UPDATE_REFRESH_TOKEN = '/api/refreshToken'
const URL_API_CHANGE_PASSSWORD = '/api/changeOwnPassword'

const URL_UPDATES_GET_LIST = '/api/updatefiles/allFiles'
const URL_UPDATES_UPLOAD_FILE = '/api/updatefiles/uploadFile'
const URL_UPDATES_DELETE_FILE = '/api/updatefiles/deleteFile/'
const URL_UPDATES_DOWNLOAD_FILE = '/api/updatefiles/downloadFile/'
const URL_UPDATES_EDIT_FILE = '/api/updatefiles/editFileInfo/'

const URL_PRICES_GET_LIST = '/api/pricelists/allFiles'
const URL_PRICES_UPLOAD_FILE = '/api/pricelists/uploadFile'
const URL_PRICES_DELETE_FILE = '/api/pricelists/deleteFile/'
const URL_PRICES_DOWNLOAD_FILE = '/api/pricelists/downloadFile/'
const URL_PRICES_EDIT_FILE = '/api/pricelists/editFileInfo/'

const URL_GET_CALC_ACTIVITY_ALL = '/api/app/allCalcActivities'
const URL_GET_CALC_ACTIVITY_FILTERED = '/api/app/calcActivityFilter'
const URL_GET_CALC_ACTIVITY_FILTERED_XLS = '/api/app/calcActivityFilterFile'
const URL_GET_CALC_ACTIVITY_DOWNLOAD_XLS = '/api/app/downloadFile/' // + {fileName}


const URL_GET_ALL_USERS = '/api/users/getAllUsers'
const URL_GET_USER = '/api/users/getUser/' //+{id}
const URL_GET_EDIT_USER = '/api/users/editUser'

const URL_GET_USER_OWN_INFO = '/api/getUserInfo'
const URL_USER_OWN_EDIT = '/api/editOwnInfo'

const URL_CHANGE_USER_PW = '/api/changeOwnPassword'

const ACCESS_TOKEN_NAME = 'accessToken'
const REFRESH_TOKEN_NAME = 'refreshToken'


const APP_ZETTA = 'z'
const APP_PRO_MEBEL = 'pm'
const APP_KOREANIKA = 'k'
const APP_KOREANIKA_MASTER = 'km'
const APP_ALL = 'all'