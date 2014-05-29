package app

import (
	"github.com/PinkFairyArmadillos/go-endpoints/endpoints"
)

func init() {

	registerApi()

	endpoints.HandleHttp()
}
