package asia.grails.sample

import jxl.DateCell
import jxl.LabelCell
import jxl.NumberCell
import jxl.Sheet
import jxl.Workbook

class PersonController {

    private final static int COLUMN_LAST_NAME = 0
    private final static int COLUMN_FIRST_NAME = 1
    private final static int COLUMN_DATE_OF_BIRTH = 2
    private final static int COLUMN_NUMBER_OF_CHILDREN = 3

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [personInstanceList: Person.list(params), personInstanceTotal: Person.count()]
    }

    def upload() {
    }

    def doUpload() {
        def file = request.getFile('file')
        Workbook workbook = Workbook.getWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheet(0);

        // skip first row (row 0) by starting from 1
        for (int row = 1; row < sheet.getRows(); row++) {
            LabelCell lastName = sheet.getCell(COLUMN_LAST_NAME, row)
            LabelCell firstName = sheet.getCell(COLUMN_FIRST_NAME, row)
            DateCell dateOfBirth = sheet.getCell(COLUMN_DATE_OF_BIRTH, row)
            NumberCell numberOfChildren = sheet.getCell(COLUMN_NUMBER_OF_CHILDREN, row)

            new Person(lastName:lastName.string , firstName:firstName.string ,
                    dateOfBirth:dateOfBirth.date, numberOfChildren:numberOfChildren.value).save()

        }
        redirect (action:'list')
    }

}
