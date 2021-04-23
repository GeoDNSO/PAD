DB_ID_KEY="_id" 
DB_USER_ID_KEY="id"#en mongo es '_id', pero principalmente no lo usaremos con usuarios, ya que el id es automatico
#Constantes para el documento de usuarios de la base de datos
DB_USERNAME_KEY="username"
DB_PASSWORD_KEY="password"
DB_EMAIL_KEY="email"

#Constantes para el documento de templates y tiers de la base de datos
DB_TITLE_KEY = "title"
DB_CATEGORY_KEY = "category"
DB_CREATOR_USERNAME_KEY = "creator_username" #Creador de la plantilla o tier dependiendo del documento
DB_CONTAINER_KEY = "container"
DB_TIER_ROWS_KEY = "tier_rows"
DB_ROW_NAME_KEY = "row_name"
DB_IMAGE_URL_KEY = "image_urls"
DB_TEMPLATE_ID = "template_id" # Para poder buscar plantillas por id

PAGINATION_PAGE = "page"
PAGINATION_LIMIT = "limit"
DEAFULT_LIMIT = 1