package app

import (
	"appengine/datastore"
	"github.com/PinkFairyArmadillos/go-endpoints/endpoints"
	"net/http"
	"time"
)

const clientId = "755334619802-7mgbbpk6vbkmim76ov2kvi0vn607j2cu.apps.googleusercontent.com"

var (
	scopes = []string{
		endpoints.EmailScope,
		"https://www.googleapis.com/auth/userinfo.profile",
	}
	clientIds = []string{clientId, endpoints.ApiExplorerClientId}
	audiences = []string{clientId}
)

// Reminder is a datastore entity that represents a single reminder.
// It also serves as (part of) a response of ReminderService.
type Reminder struct {
	Id       string    `json:"id,omitempty" datastore:"-"`
	User     string    `json:"user" datastore:"User"`
	Title    string    `json:"title" datastore:",noindex"`
	Location []float64 `json:"location" datastore:",noindex"`
	Reminder []string  `json:"reminder" datastore:",noindex"`
	Date     time.Time `json:"date"`
	Urgency  int       `json:"urgency"`
}

// ReminderService
type ReminderService struct {
}

// ReminderList is a response type of ReminderService.List method
type RemindersList struct {
	Items []*Reminder `json:"items"`
}

type ReminderUserQuery struct {
	UserName string `json:"username" endpoints:"required"`
}

// List responds with a list of all reminders ordered by Date field.
// Most recent reminders come first.
func (gs *ReminderService) List(
	r *http.Request, req *ReminderUserQuery, resp *RemindersList) error {

	c := endpoints.NewContext(r)

	username := req.UserName

	q := datastore.NewQuery("Reminder").Filter("User =", username)
	reminder := make([]*Reminder, 0, 10)
	keys, err := q.GetAll(c, &reminder)
	if err != nil {
		return err
	}
	for i, k := range keys {
		reminder[i].Id = k.Encode()
	}
	resp.Items = reminder
	return nil
}

// NewReminder is the expected data structure
type NewReminder struct {
	List     []string  `json:"reminder" endpoints:"required"`
	Lat      float64   `json:"latitude" endpoints:"required"`
	Lng      float64   `json:"longitude" endpoints:"required"`
	UserName string    `json:"username" endpoints:"required"`
	Time     time.Time `json:"due date/time" endpoints:"required"`
	Title    string    `json:"title" endpoints:"required"`
	Urgency  int       `json:"urgency" endpoints:"required"`
}

// createReminder creates a new Reminder based on provided NewReminder.
// It stores newly created Reminder with Content being that of NewReminder.Reminder.
// User field will have current username
func (gs *ReminderService) CreateReminder(
	r *http.Request, req *NewReminder, reminder *Reminder) error {

	c := endpoints.NewContext(r)

	reminder.Reminder = make([]string, 1)
	reminder.Reminder = append(req.List)
	reminder.Location = make([]float64, 0)
	reminder.Location = append(reminder.Location, req.Lat, req.Lng)
	reminder.Date = time.Now()
	reminder.User = req.UserName
	reminder.Title = req.Title
	reminder.Urgency = req.Urgency

	key, err := datastore.Put(
		c, datastore.NewIncompleteKey(c, "Reminder", nil), reminder)
	if err != nil {
		return err
	}

	reminder.Id = key.Encode()
	return nil
}

// ReminderIdReq serves as a data structure for identifying a single Reminder.
type ReminderIdReq struct {
	Id string `json:"id" endpoints:"required"`
}

// Delete deletes a single reminder
func (gs *ReminderService) Delete(
	r *http.Request, req *ReminderIdReq, _ *endpoints.VoidMessage) error {

	c := endpoints.NewContext(r)
	key, err := datastore.DecodeKey(req.Id)
	if err != nil {
		return err
	}
	return datastore.Delete(c, key)
}

func registerApi() (*endpoints.RpcService, error) {
	reminderService := &ReminderService{}
	rpcService, err := endpoints.RegisterServiceWithDefaults(reminderService)
	if err != nil {
		return nil, err
	}
	rpcService.Info().Name = "reminders"

	info := rpcService.MethodByName("List").Info()
	info.Name, info.HttpMethod, info.Path, info.Desc =
		"reminders.list", "GET", "reminders/list", "List most recent reminders."

	info = rpcService.MethodByName("CreateReminder").Info()
	info.Name, info.HttpMethod, info.Path, info.Desc =
		"reminders.createReminder", "POST", "reminders/createreminder", "Create a Reminder."
	info.Scopes = scopes
	info.Audiences = audiences
	info.ClientIds = clientIds

	info = rpcService.MethodByName("Delete").Info()
	info.Name, info.HttpMethod, info.Path, info.Desc =
		"reminders.delete", "DELETE", "reminders/delete/{id}", "Delete a single reminder."

	return rpcService, nil
}
